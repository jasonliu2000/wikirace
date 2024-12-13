# Build the frontend
FROM node:18-alpine AS build

WORKDIR /app

COPY package.json package-lock.json ./

RUN npm install

COPY . .

ENV REACT_APP_API_URL="http://localhost:8081"

RUN npm run build

# Serve static frontend build using nginx
FROM nginx:stable-alpine

COPY --from=build /app/build /usr/share/nginx/html

EXPOSE 8080

# Start nginx when the container runs
CMD ["nginx", "-g", "daemon off;"]
