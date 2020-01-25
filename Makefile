repl:
	cd client && rm -rf .cpcache/ && clj -A:dev:nrepl

build:
	cd client && clj -m build

docker-up:
	docker-compose up -d

docker-down:
	docker-compose down
