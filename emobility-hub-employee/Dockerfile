FROM node:18-alpine AS build

WORKDIR /app

COPY package*.json ./
RUN npm install --legacy-peer-deps

COPY . .

# Override environment files - replace localhost with Docker service names
RUN printf 'export const environment = {\n  baseUrl: "",\n  cars: "/electric-cars",\n  parseVehicle: "/parse-vehicle",\n  manufacturers: "/manufacturers",\n  bicycles: "/electric-bicycles",\n  scooters: "/electric-scooters",\n  clients: "/clients",\n  employees: "/users",\n  faults: "/faults",\n  rentals: "/rentals",\n  statistics: "/statistics",\n  auth: "/auth",\n};\n' > src/app/config/environment.ts

RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist/emobility-hub-employee/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]