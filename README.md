# Sport Events demo application


1. Prerequisites

   A working and configured Docker is needed.

2. To run the project, use the following command:

    docker-compose up

---

Data Storing REST api will be available at http://localhost:8080

Swagger Doc: http://localhost:8080/swagger-ui/index.html

Health: http://localhost:8080/actuator/health

---

Data Retrieving REST api will be available at http://localhost:8081

Swagger Doc: http://localhost:8081/swagger-ui/index.html

Health: http://localhost:8081/actuator/health

---

### Request examples:

Create Sport Event:

```
curl --request POST \
  --url http://localhost:8080/events \
  --header 'Content-Type: application/json' \
  --data '{
	"description": "Arsenal v Manchester United",
	"homeTeam": "Arsenal",
	"awayTeam": "Manchester United",
	"startTime": "31/01/2025 18:00",
	"sport": "Football",
	"country": "England",
	"competition": "Premier League",
	"settled": false,
	"markets": [
		{
			"description": "Match Betting",
			"status": "OPEN",
			"settled": false,
			"outcomes": [
				{
					"description": "Arsenal to win",
					"settled": false,
					"price": 2.0,
					"result": null
				}
			]
		}
	]
}'
```

Update Sport Event:

```
curl --request PUT \
  --url http://localhost:8080/events \
  --header 'Content-Type: application/json' \
  --data '{
	"id": 1,
	"description": "Arsenal v Manchester United",
	"homeTeam": "Arsenal",
	"awayTeam": "Manchester United",
	"startTime": "31/01/2025 18:00",
	"sport": "Football",
	"country": "England",
	"competition": "Premier League",
	"settled": true,
	"markets": [
		{
			"id": 1,
			"description": "Match Betting",
			"status": "OPEN",
			"settled": true,
			"outcomes": [
				{
					"id": 1,
					"description": "Arsenal to win",
					"settled": true,
					"price": 2.0,
					"result": "WIN"
				}
			]
		}
	]
}'
```


Delete Sport Event:

```
curl --request DELETE \
  --url http://localhost:8080/events/1
```


Get Non-settled Events:

```
curl --request GET \
  --url 'http://localhost:8081/events/non-settled' \
  --header 'Content-Type: application/json'
```  

Get Non-settled Events By Sport:

```
curl --request GET \
  --url 'http://localhost:8081/events/non-settled?sport=Football' \
  --header 'Content-Type: application/json'
```
