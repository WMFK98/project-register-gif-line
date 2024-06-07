FROM nginx:alpine AS run
COPY ./reverse-proxy.conf /etc/nginx/conf.d/default.conf
