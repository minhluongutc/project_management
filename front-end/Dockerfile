FROM node:18.17.0

WORKDIR /usr/app
COPY ./ /usr/app


RUN npm install

COPY . .
CMD [ "npm", "start" ]
