# Camcorders
Aggregation of data from multiple services 

The task - 
write code that receives and aggregates data from multiple services.

CCTV cameras were select as the object of aggregation.
List of available cameras is locate at the link  - http://www.mocky.io/v2/5c51b9dd3400003252129fb5
Specified link contains a string in the format JSON with fields:

id - identifier of camera,  
sourceDataUrl - object data reference,  
tokenDataUrl - object data reference.   

By the links specified in the fields - sourceDataUrl and tokenDataUrl - get additional information for the object. Then aggregate the received information into an object.

Original JSON string:

[
    {
        "id": 1,
        "sourceDataUrl": "http://www.mocky.io/v2/5c51b230340000094f129f5d",
        "tokenDataUrl": "http://www.mocky.io/v2/5c51b5b6340000554e129f7b?mocky-delay=1s"
    },
    {
        "id": 20,
        "sourceDataUrl": "http://www.mocky.io/v2/5c51b2e6340000a24a129f5f?mocky-delay=100ms",
        "tokenDataUrl": "http://www.mocky.io/v2/5c51b5ed340000554e129f7e"
    },
    {
        "id": 3,
        "sourceDataUrl": "http://www.mocky.io/v2/5c51b4b1340000074f129f6c",
        "tokenDataUrl": "http://www.mocky.io/v2/5c51b600340000514f129f7f?mocky-delay=2s"
    },
    {
        "id": 2,
        "sourceDataUrl": "http://www.mocky.io/v2/5c51b5023400002f4f129f70",
        "tokenDataUrl": "http://www.mocky.io/v2/5c51b623340000404f129f82"
    }
]

Expected result:

[
    {
        "id": 1,
        "urlType": "LIVE",
        "videoUrl": "rtsp://127.0.0.1/1",
        "value": "fa4b588e-249b-11e9-ab14-d663bd873d93",
        "ttl": 120
    },
    {
        "id": 3,
        "urlType": "ARCHIVE",
        "videoUrl": "rtsp://127.0.0.1/3",
        "value": "fa4b5d52-249b-11e9-ab14-d663bd873d93",
        "ttl": 120
    },
    {
        "id": 20,
        "urlType": "LIVE",
        "videoUrl": "rtsp://127.0.0.1/20",
        "value": "fa4b5f64-249b-11e9-ab14-d663bd873d93",
        "ttl": 180
    },
    {
        "id": 2,
        "urlType": "ARCHIVE",
        "videoUrl": "rtsp://127.0.0.1/2",
        "value": "fa4b5b22-249b-11e9-ab14-d663bd873d93",
        "ttl": 60
    }
]

One must take into account the possible large amounts of data, the collection
and the aggregation must be done in several threads.

Technologies are use in the class CameraDataAggregator:

HttpClient - for to send GET request and retrieve response asynchronously.
CompletableFuture<> - to save and retrieve the result.
ObjectMapper - to retrieve and create an object from a JSON string. 
ExecutorService - for thread control and execution in multiple threads.
FutureTask<> - to perform aggregation in a separate thread.