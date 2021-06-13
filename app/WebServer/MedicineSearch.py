# -*- encoding:utf-8 -*-

import sys
from importlib import reload
reload(sys)

import requests

service_key = 'VT4PHacpcZrTbACxd6dqjs3jQrKb1VSBep1jwz75R7QcRztCxN2SB4DcdJEfVfDkTcvBwxfMH%2BgomRkaYNAPiQ%3D%3D'


# Receive medicine name from user and return information(list or specific information) of medicine
# Parameter: Name of medicine user wants to search
# Return: Information of medicine
def crawler(medicine_name):
    jsonUrl = f"http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?serviceKey={service_key}&pageNo=1&numOfRows=3&itemName={medicine_name}&type=json"

    response = requests.get(jsonUrl)
    html = response.text.replace('null', '""')

    bError = False

    try:
        if True:
            body = eval(html)['body']
            if 'items' in body:
                return eval(html)['body']['items']
            else:
                bError = True
    except SyntaxError:
        bError = True

    if bError:
        return []
    else:
        raise ConnectionError('Unknown error')


def search_item_by_id(id):
    jsonUrl = f"http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?serviceKey={service_key}&pageNo=1&numOfRows=3&itemSeq={id}&type=json"

    response = requests.get(jsonUrl)
    html = response.text.replace('null', '""')

    if '<OpenAPI_ServiceResponse>' in html:
        return []

    body = eval(html)['body']
    if 'items' in body:
        return eval(html)['body']['items']
    else:
        return []
