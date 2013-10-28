#!/usr/bin/env python


import requests
import json

#test_url = "http://localhost:5000/send"
test_url = "http://desolate-sands-4774.herokuapp.com/send"

headers = {'Content-type': 'application/json'}

recv= [ "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25",
       "26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51"]

for n in range (0,len(recv)+1) :
    data = {"originator" : "roger", "message" : "Bill strange things are afoot at the Circle K." , "receivers" : recv[:n] }
    data_json = json.dumps(data)
    r = requests.post(test_url, data=data_json, headers=headers)
    v = json.loads(r.content)
    print v
    

r = requests.get(test_url)
print r.status_code

r = requests.put(test_url)
print r.status_code

r = requests.delete(test_url)
print r.status_code
# bad/unexpected JSON

bad = { "foo" : 4 }
bad_json = json.dumps(bad)
r = requests.post(test_url, data=bad_json, headers=headers)

print r.status_code
