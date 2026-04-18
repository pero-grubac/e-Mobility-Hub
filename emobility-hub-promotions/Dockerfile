FROM tomcat:9.0-jdk21

# Remove default Tomcat applications
RUN rm -rf /usr/local/tomcat/webapps/*

WORKDIR /build

COPY src/ src/
COPY lib/ lib/

# Override app.properties - replace localhost with backend service name
RUN echo "LOGIN_URL=http://backend:8080/auth/login" > \
    src/main/java/org/unibl/etf/emobility_hub_promotions/properties/app.properties && \
    echo "PROMOTIONS_URL=http://backend:8080/promotions" >> \
    src/main/java/org/unibl/etf/emobility_hub_promotions/properties/app.properties && \
    echo "ANNOUNCEMENTS_URL=http://backend:8080/announcements" >> \
    src/main/java/org/unibl/etf/emobility_hub_promotions/properties/app.properties

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
    cp src/main/webapp/WEB-INF/web.xml /build/war/WEB-INF/ && \
    find src/main/webapp -maxdepth 1 ! -name "WEB-INF" ! -name "META-INF" ! -name "webapp" \
         -exec cp -r {} /build/war/ \; 2>/dev/null || true

# Package as WAR and deploy to Tomcat
RUN cd /build/war && jar -cvf /usr/local/tomcat/webapps/promotions.war .

EXPOSE 8080
CMD ["catalina.sh", "run"]