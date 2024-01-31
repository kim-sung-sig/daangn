import pandas as pd
import json
# 엑셀 파일 경로
excel_file_path = 'bbbbb.xlsx'

# 엑셀 파일 읽기
df = pd.read_excel(excel_file_path, engine='openpyxl')

# JSON으로 변환할 데이터 구조 생성
result_dict = {}

# 데이터프레임을 순회하며 JSON 데이터 생성
for index, row in df.iterrows():
    city = row['시도']
    district = row['시군구']
    town = row['행정구역명']
    if city == district or city == town or district == town:
        continue

    # result_dict에 city 키가 없으면 추가
    if city not in result_dict:
        result_dict[city] = {}

    # result_dict[city]에 district 키가 없으면 추가
    if district not in result_dict[city]:
        result_dict[city][district] = []

    # result_dict[city][district]에 town 추가
    if town not in result_dict[city][district]:
        result_dict[city][district].append(town)

# JSON으로 변환
json_data = json.dumps(result_dict, ensure_ascii=False, indent=4)

# JSON 데이터 출력
print(json_data)

with open('data.json', 'w', encoding='utf-8') as json_file:
    json_file.write(json_data)
