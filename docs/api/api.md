---
title: 12306 v1.0.0
language_tabs:
  - shell: Shell
  - http: HTTP
  - javascript: JavaScript
  - ruby: Ruby
  - python: Python
  - php: PHP
  - java: Java
  - go: Go
toc_footers: []
includes: []
search: true
code_clipboard: true
highlight_theme: darkula
headingLevel: 2
generator: "@tarslib/widdershins v4.0.17"

---

# 12306

> v1.0.0

Base URLs:

* <a href="http://localhost:8000">开发环境: http://localhost:8000</a>

# 登录注册相关

## GET 获取用户总数

GET /member/member/count

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|token|header|string| 是 |none|

> 返回示例

> 200 Response

```json
{
  "success": true,
  "message": null,
  "data": 0
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» success|boolean|true|none||none|
|» message|null|true|none||none|
|» data|integer|true|none||none|

## POST 用户注册

POST /member/member/register

> Body 请求参数

```json
{
  "mobile": "string"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» mobile|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{
  "success": true,
  "message": null,
  "data": 0
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» success|boolean|true|none||none|
|» message|null|true|none||none|
|» data|integer|true|none||none|

## POST 发送验证码

POST /member/member/send-code

> Body 请求参数

```json
{
  "mobile": "string"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» mobile|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{
  "success": true,
  "message": null,
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» success|boolean|true|none||none|
|» message|null|true|none||none|
|» data|null|true|none||none|

## POST 用户登录

POST /member/member/login

> Body 请求参数

```json
{
  "mobile": "string",
  "code": "string"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» mobile|body|string| 是 |none|
|» code|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{
  "success": true,
  "message": null,
  "data": {
    "id": 0,
    "mobile": "string",
    "token": "string"
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» success|boolean|true|none||none|
|» message|null|true|none||none|
|» data|object|true|none||none|
|»» id|integer|true|none||none|
|»» mobile|string|true|none||none|
|»» token|string|true|none||none|

# 乘车人相关

## POST 新增/更新 乘车人信息

POST /member/passenger/save

> Body 请求参数

```json
{
  "id": 0,
  "name": "string",
  "idCard": "string",
  "type": "1"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|token|header|string| 否 |none|
|body|body|object| 否 |none|
|» id|body|integer| 否 |存在id时为更新 不存在时为新增|
|» name|body|string| 是 |none|
|» idCard|body|string| 是 |none|
|» type|body|string| 是 |1:成人 2:儿童 3:学生|

#### 枚举值

|属性|值|
|---|---|
|» type|1|
|» type|2|
|» type|3|

> 返回示例

> 200 Response

```json
{
  "success": true,
  "message": null,
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» success|boolean|true|none||none|
|» message|null|true|none||none|
|» data|null|true|none||none|

## GET 查询乘车人信息

GET /member/passenger/query-list

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|page|query|string| 否 |none|
|size|query|string| 否 |none|
|token|header|string| 是 |none|

> 返回示例

> 200 Response

```json
{
  "success": true,
  "message": null,
  "data": {
    "total": 0,
    "list": [
      {
        "id": "string",
        "memberId": "string",
        "name": "string",
        "idCard": "string",
        "type": "string",
        "createTime": "string",
        "updateTime": "string"
      }
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» success|boolean|true|none||none|
|» message|null|true|none||none|
|» data|object|true|none||none|
|»» total|integer|true|none||none|
|»» list|[object]|true|none||none|
|»»» id|string|true|none||none|
|»»» memberId|string|true|none||none|
|»»» name|string|true|none||none|
|»»» idCard|string|true|none||none|
|»»» type|string|true|none||none|
|»»» createTime|string|true|none||none|
|»»» updateTime|string|true|none||none|

## DELETE 删除乘车人信息

DELETE /member/passenger/delete/{id}

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|id|path|string| 是 |none|
|token|header|string| 否 |none|

> 返回示例

> 200 Response

```json
{
  "success": true,
  "message": null,
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» success|boolean|true|none||none|
|» message|null|true|none||none|
|» data|null|true|none||none|

## GET 获取乘车人列表

GET /member/passenger/query-mine

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|token|header|string| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

# 车站相关

## GET 获取所有车站

GET /business/station/query-all

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|token|header|string| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## GET 获取所有车站（管理员）

GET /business/admin/station/query-all

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## GET 分页获取车站（管理员）

GET /business/admin/station/query-list

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|page|query|string| 否 |none|
|size|query|string| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## DELETE 删除车站（管理员）

DELETE /business/admin/station/delete/{id}

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|id|path|string| 是 |none|

> 返回示例

> 200 Response

```json
{
  "success": true,
  "message": null,
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» success|boolean|true|none||none|
|» message|null|true|none||none|
|» data|null|true|none||none|

## POST 新增/修改 车站信息（管理员）

POST /business/admin/station/save

> Body 请求参数

```json
{
  "id": "string",
  "name": "string",
  "namePinyin": "string",
  "namePy": "string"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» id|body|string| 否 |存在id时为更新 不存在时为新增|
|» name|body|string| 是 |none|
|» namePinyin|body|string| 是 |none|
|» namePy|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## POST 批量新增车站（管理员）

POST /business/admin/station/save-list

> Body 请求参数

```json
{}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

# 车次相关

## GET 获取所有车次

GET /business/train/query-all

> Body 请求参数

```yaml
{}

```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|token|header|string| 否 |none|
|body|body|object| 否 |none|

> 返回示例

> 200 Response

```json
{
  "success": true,
  "message": null,
  "data": [
    "string"
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» success|boolean|true|none||none|
|» message|null|true|none||none|
|» data|[string]|true|none||none|

## GET 获取所有车次（管理员）

GET /business/admin/train/query-all

> 返回示例

> 200 Response

```json
{
  "success": true,
  "message": null,
  "data": [
    "string"
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» success|boolean|true|none||none|
|» message|null|true|none||none|
|» data|[string]|true|none||none|

## GET 分页获取车次数据（管理员）

GET /business/admin/train/query-list

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|page|query|string| 否 |none|
|size|query|string| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## DELETE 根据ID删除车次（管理员）

DELETE /business/admin/train/delete/{id}

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|id|path|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## POST 新增/修改 车次信息（管理员）

POST /business/admin/train/save

> Body 请求参数

```json
{
  "id": "string",
  "code": "string",
  "type": "string",
  "start": "string",
  "startPinyin": "string",
  "end": "string",
  "endPinyin": "string",
  "startTime": "string",
  "endTime": "string"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» id|body|string| 否 |存在id时为更新 不存在时为新增|
|» code|body|string| 是 |none|
|» type|body|string| 是 |none|
|» start|body|string| 是 |none|
|» startPinyin|body|string| 是 |none|
|» end|body|string| 是 |none|
|» endPinyin|body|string| 是 |none|
|» startTime|body|string| 是 |none|
|» endTime|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

# 车次相关站点（管理员）

## GET 获取列车相关所有车站信息（管理员）

GET /business/admin/train-station/query-list

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|page|query|string| 否 |none|
|size|query|string| 否 |none|
|trainCode|query|string| 否 |none|

> 返回示例

> 200 Response

```json
{
  "success": true,
  "message": null,
  "data": {
    "total": 0,
    "list": [
      "string"
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» success|boolean|true|none||none|
|» message|null|true|none||none|
|» data|object|true|none||none|
|»» total|integer|true|none||none|
|»» list|[string]|true|none||none|

## DELETE 删除车次相关站点信息（管理员）

DELETE /business/admin/train-station/delete/{id}

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|id|path|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## POST 新增\修改 车次相关站点信息（管理员）

POST /business/admin/train-station/save

> Body 请求参数

```json
{
  "id": "string",
  "trainCode": "string",
  "index": 0,
  "name": "string",
  "namePinyin": "string",
  "inTime": "string",
  "outTime": "string",
  "stopTime": "string",
  "km": 0
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» id|body|string| 否 |none|
|» trainCode|body|string| 是 |none|
|» index|body|integer| 是 |none|
|» name|body|string| 是 |none|
|» namePinyin|body|string| 是 |none|
|» inTime|body|string| 是 |none|
|» outTime|body|string| 是 |none|
|» stopTime|body|string| 是 |none|
|» km|body|integer| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

# 车厢相关（管理员）

## GET 查询所有车厢（管理员）

GET /business/admin/train-carriage/query-list

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|page|query|string| 否 |none|
|size|query|string| 否 |none|
|trainCode|query|string| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## DELETE 删除选中车厢（管理员）

DELETE /business/admin/train-carriage/delete/{id}

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|id|path|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## POST 新增/更新 车厢信息（管理员）

POST /business/admin/train-carriage/save

> Body 请求参数

```json
{
  "id": "string",
  "trainCode": "string",
  "index": "string",
  "seatType": "string",
  "seatCount": "string",
  "rowCount": "string",
  "colCount": "string"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» id|body|string| 否 |none|
|» trainCode|body|string| 是 |none|
|» index|body|string| 是 |none|
|» seatType|body|string| 是 |none|
|» seatCount|body|string| 否 |none|
|» rowCount|body|string| 是 |none|
|» colCount|body|string| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

# 列车座位相关（管理员）

## GET 获取座位列表（管理员）

GET /business/admin/train-seat/query-list

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|page|query|string| 否 |none|
|size|query|string| 否 |none|
|trainCode|query|string| 否 |列车号|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## POST 新增/修改 座位信息（管理员）

POST /business/admin/train-seat/save

> Body 请求参数

```json
{
  "id": "string",
  "trainCode": "string",
  "carriageIndex": 0,
  "row": "string",
  "col": "string",
  "seatType": "string",
  "carriageSeatIndex": 0
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» id|body|string| 否 |none|
|» trainCode|body|string| 是 |none|
|» carriageIndex|body|integer| 是 |none|
|» row|body|string| 是 |none|
|» col|body|string| 是 |none|
|» seatType|body|string| 是 |none|
|» carriageSeatIndex|body|integer| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## DELETE 删除座位信息（管理员）

DELETE /business/admin/train-seat/delete/{id}

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|id|path|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

# 每日车次数据管理（管理员）

## GET 查询每日车次数据（管理员）

GET /business/admin/daily-train/query-list

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|page|query|string| 否 |none|
|size|query|string| 否 |none|
|code|query|string| 否 |车次|
|date|query|string| 否 |日期：yyyy-MM-dd|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## POST 新增/更新每日车次数据（管理员）

POST /business/admin/daily-train/save

> Body 请求参数

```json
{
  "id": "string",
  "date": "string",
  "code": "string",
  "type": "string",
  "start": "string",
  "startPinyin": "string",
  "startTime": "string",
  "end": "string",
  "endPinyin": "string",
  "endTime": "string"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» id|body|string| 否 |none|
|» date|body|string| 是 |none|
|» code|body|string| 是 |none|
|» type|body|string| 是 |none|
|» start|body|string| 是 |none|
|» startPinyin|body|string| 是 |none|
|» startTime|body|string| 是 |none|
|» end|body|string| 是 |none|
|» endPinyin|body|string| 是 |none|
|» endTime|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## DELETE 删除每日车次数据（管理员）

DELETE /business/admin/daily-train/delete/{id}

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|id|path|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

# 数据模型

