prepare:
	docker-compose up -d
	bash wait.sh

run:
	./mvnw clean package && \
    docker build -t medicament-outlet . && \
    docker run --env-file .env --rm -p 8080:8080 --network=medicament-outlet_system --name app medicament-outlet

clean:
	docker ps -a | grep app && docker stop app && docker rm app || true
	docker-compose down && \
	./mvnw clean

migrate:
	./mvnw spring-boot:run -Dconsole=true -Dspring-boot.run.profiles=console-application
