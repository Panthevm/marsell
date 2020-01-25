FROM nginx:latest
COPY ./resources/public/index.html /usr/share/nginx/html/
COPY ./build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
ENTRYPOINT ["nginx","-g","daemon off;"]