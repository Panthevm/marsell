##UI

ubuild:
	cd client && clj -A:prod

##SERVER

sbuild:
  cd server && clj -A:uberjar

sgraph:
		cd server && clj -A:graph -o deps.png --size

srun:
	cd server && clj -m app.core

srepl:
	cd server && rm -rf .cpcache/ && clj -A:test:nrepl

stest:
	cd server && clj -A:test:kaocha


##CI

docker-up:
	docker-compose up -d

docker-down:
	docker-compose down



