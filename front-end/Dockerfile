# Stage 1: Building the Angular application
FROM node:18.17.0 as builder

# Set the working directory inside the Docker image
WORKDIR /usr/src/app

# Copy package.json and package-lock.json (if available)
COPY package.json package-lock.json* ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the Angular application in production mode
RUN npm run build

# Optionally add this line to debug and list what's in the dist folder
RUN ls -al /usr/src/app/dist/front-end

# Stage 2: Serve the app with Nginx
FROM nginx:alpine

# Copy the build output from the builder stage
COPY --from=builder /usr/src/app/dist/front-end /usr/share/nginx/html

# Optional: This line is for debugging to check if files are copied correctly
RUN ls -al /usr/share/nginx/html

# Copy the Nginx configuration file into the image
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80 to the host so Nginx can serve the app
EXPOSE 80

# Start Nginx and keep it running
CMD ["nginx", "-g", "daemon off;"]