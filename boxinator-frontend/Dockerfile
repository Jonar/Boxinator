# Build stage
FROM node:12 as build
WORKDIR /app
COPY package.json ./
COPY package-lock.json ./
RUN npm ci
COPY . ./
RUN npm run build

# Deploy stage
FROM nginx
COPY --from=build /app/build /usr/share/nginx/html