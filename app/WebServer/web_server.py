# -*- encoding:utf-8 -*-

import sys
from importlib import reload

reload(sys)
import pprint
import MedicineSearch
from flask import Flask
from flask import request
from flask import render_template


app = Flask(__name__)

@app.route('/')
def hello_world():
    result = {}
    if "userID" in request.cookies:
        result["login"] = {"bool": "true"}
        if len(result["memos"]) < 4:
            result["memos1"] = result["memos"]
            result["memos2"] = []
        else:
            result["memos1"] = result["memos"][:4]
            if len(result["memos"]) < 8:
                result["memos2"] = result["memos"][4:]
            else:
                result["memos2"] = result["memos"][4:8]
    else:
        result["login"] = {"bool": "false"}
        result["memos1"] = []
        result["memos2"] = []
    return render_template('index.html')


# 약물 검색
@app.route('/search/medicine', methods=['POST'])
def search_medicine():
    medicine_name = request.form['medicine_name']
    result = {}
    result["medicines"] = MedicineSearch.crawler(medicine_name)
    result["login"] = {"bool": "None"}
    pprint.pprint(result)
    return render_template('Medicine_list.html', result=result)


if __name__ == '__main__':
    app.run()
