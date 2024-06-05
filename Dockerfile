FROM node:latest AS build
COPY package*.json .
RUN npm install 
COPY . .
RUN npm run build

FROM nginx:latest AS run
COPY --from=build /dist /usr/share/nginx/html
COPY /default.conf /etc/nginx/conf.d/default.conf
COPY /certificate.crt /etc/nginx
COPY /private.key /etc/nginx
