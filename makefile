##UI
ubuild:
	cd client && clj -A:prod
utest:
	cd client && clj -A:test:kaocha
##SERVER

sbuild:
        cd server && clj -A:uberjar
sgraph:
	cd server && clj -A:graph -o deps.png --size
sjar:
	cd server && java -cp target/server.jar clojure.main -m app.core
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



