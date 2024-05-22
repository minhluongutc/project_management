FROM node:21.7.1

COPY ["package.json", "./"]

RUN npm install

COPY . .

RUN npm build

CMD [ "npm", "start" ]