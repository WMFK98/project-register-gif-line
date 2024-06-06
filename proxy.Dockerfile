FROM nginx:alpine AS run
COPY ./reverse_proxy.conf /etc/nginx/conf.d/default.conf