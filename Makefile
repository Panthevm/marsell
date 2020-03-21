#Repl
ui-repl:
	cd client && rm -rf .cpcache/ && npx shadow-cljs watch app

back-repl:
	cd server && rm -rf .cpcache/ && clj -A:nrepl

#Run
back-run:
	cd server && clj -m app.rest

#Build
ui-build:
	cd client && npx shadow-cljs release prod

#Docker
docker-up:
	docker-compose up -d

docker-down:
	docker-compose down
