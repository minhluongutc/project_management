FROM node:21.7.1

COPY ["package.json", "./"]

COPY .env .

RUN npm install

COPY . .

RUN npm build

CMD [ "npm", "start" ]