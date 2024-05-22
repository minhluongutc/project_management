FROM node:21.7.1

COPY ["package.json", "./"]

COPY .env .

RUN yarn install

COPY . .

RUN yarn build

CMD [ "yarn", "start:prod" ]