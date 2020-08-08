##UI

ubuild:
	cd client && clj -A:prod

##SERVER

sbuild:
  cd server && clj -A:uberjar

#sjar:
 # cd server && java -cp target/cdeps-0.1.0.jar clojure.main -m app.rest

srun:
	cd server && clj -m app.rest

srepl:
	cd server && rm -rf .cpcache/ && clj -A:test:nrepl

stest:
	cd server && clj -A:test:kaocha


##CI

docker-up:
	docker-compose up -d

docker-down:
	docker-compose down



