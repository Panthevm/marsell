#Repl
repl:
	cd client && rm -rf .cpcache/ && clj -A:dev:nrepl

back-repl:
	cd server && clj -A:nrepl

#Build
build:
	cd client && clj -m build

#Docker
docker-up:
	docker-compose up -d

docker-down:
	docker-compose down
