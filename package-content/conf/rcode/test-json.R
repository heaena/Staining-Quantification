library(rjson)
json <- fromJSON('{
                    "id":"1",
                    "name":["Google","Runoob","Taobao"],
                    "url":["www.google.com","www.runoob.com","www.taobao.com"],
                    "likes":[ 111,222,333]
                 }')

print(json$id)
