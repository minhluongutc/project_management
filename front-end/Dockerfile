FROM node:18.17.0

COPY ["package.json", "./"]

RUN npm install

COPY . .
CMD [ "npm", "start" ]
