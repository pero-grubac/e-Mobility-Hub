FROM tomcat:9.0-jdk21

# Remove default Tomcat applications
RUN rm -rf /usr/local/tomcat/webapps/*

WORKDIR /build

COPY src/ src/
COPY lib/ lib/

# Override ConnectionPool.properties - replace localhost with mysql service name
RUN echo "jdbcURL=jdbc:mysql://mysql:3306/emobility?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC" > src/main/java/org/unibl/etf/emobility_hub_user/dao/ConnectionPool.properties && \
    echo "username=student" >> src/main/java/org/unibl/etf/emobility_hub_user/dao/ConnectionPool.properties && \
    echo "password=student" >> src/main/java/org/unibl/etf/emobility_hub_user/dao/ConnectionPool.properties && \
    echo "driver=com.mysql.cj.jdbc.Driver" >> src/main/java/org/unibl/etf/emobility_hub_user/dao/ConnectionPool.properties && \
    echo "preconnectCount=2" >> src/main/java/org/unibl/etf/emobility_hub_user/dao/ConnectionPool.properties && \
    echo "maxIdleConnections=25" >> src/main/java/org/unibl/etf/emobility_hub_user/dao/ConnectionPool.properties && \
    echo "maxConnections=25" >> src/main/java/org/unibl/etf/emobility_hub_user/dao/ConnectionPool.properties

# Copy JAR dependencies into WEB-INF/lib
RUN mkdir -p src/main/webapp/WEB-INF/lib && \
    cp lib/*.jar src/main/webapp/WEB-INF/lib/ 2>/dev/null || true

# Compile Java source files
RUN mkdir -p classes && \
    find src/main/java -name "*.java" > sources.txt && \
    javac -cp "$(find /usr/local/tomcat/lib -name '*.jar' | tr '\n' ':')$(find src/main/webapp/WEB-INF/lib -name '*.jar' | tr '\n' ':')" \
          -d classes \
          @sources.txt

# Copy properties files into classes directory
RUN find src/main/java -name "*.properties" | while read f; do \
        dest="classes/${f#src/main/java/}"; \
        mkdir -p "$(dirname $dest)"; \
        cp "$f" "$dest"; \
    done

# Build WAR structure
RUN mkdir -p /build/war/WEB-INF/classes && \
    cp -r classes/* /build/war/WEB-INF/classes/ && \
    cp -r src/main/webapp/WEB-INF/lib /build/war/WEB-INF/ && \
    cp -r src/main/webapp/WEB-INF/pages /build/war/WEB-INF/ && \
    cp src/main/webapp/WEB-INF/web.xml /build/war/WEB-INF/ && \
    cp -r src/main/webapp/images /build/war/ && \
    cp -r src/main/webapp/css /build/war/ && \
    cp -r src/main/webapp/js /build/war/ && \
    find src/main/webapp -maxdepth 1 ! -name "WEB-INF" ! -name "META-INF" ! -name "webapp" \
         -exec cp -r {} /build/war/ \; 2>/dev/null || true

# Package as WAR and deploy to Tomcat
RUN cd /build/war && jar -cvf /usr/local/tomcat/webapps/user.war .

EXPOSE 8080
CMD ["catalina.sh", "run"]