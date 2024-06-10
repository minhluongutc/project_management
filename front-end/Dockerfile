# Stage 1: Build the Angular application
FROM node:18.17.0 as build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . ./
RUN npm run build

# Stage 2: Serve the application using http-server
FROM node:18.17.0
WORKDIR /app
COPY --from=build /app/dist/front-end/browser .
RUN npm install -g http-server
EXPOSE 4321
CMD ["http-server", "-p 4321"]