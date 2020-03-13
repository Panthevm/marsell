#Repl
ui-repl:
	cd client && rm -rf .cpcache/ && clj -A:dev:nrepl

back-repl:
	cd server && rm -rf .cpcache/ && clj -A:nrepl

#Run
back-run:
	cd server && clj -m app.rest

#Build
build:
	cd client && clj -A:prod

#Docker
docker-up:
	docker-compose up -d

docker-down:
	docker-compose down
