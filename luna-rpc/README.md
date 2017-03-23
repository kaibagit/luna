# Luna RPC
一款参考自dubbo和motan的微服务框架。

# Features
## 自定义通讯协议luna，可自行拓展
| 8bit | 8bit  | 8bit  | 8bit
|--|--|--|--
|00-07|08-15|16-23|24-31
|magic | <<    | flag  | status
|32-39|40-47 |48-55 |56-63
|request_id| << | << | <<
|64-71|72-79 |80-87 |88-95
| << | << | << | <<
|96-103|104-111|112-119|120-127
|body_length| << | << | <<

flag包含：hb（心跳标示）、ow(OneWay)、rp（请求还是响应）

status（响应状态）：0x00（调用成功）、0x03（调用抛出异常）、0x05（调用没有返回值）

request_id 长度为64位

body_length 长度为32位

## 支持多种序列化方式
- Hessian2
- Json
- Thrift

## 支持多种负载均衡算法
- 轮询
- 根据权重负载均衡



## To-do List
- [ ] LeastActiveLoadBalance
- [ ] 支持跨语言

### 跨语言数据类型设计
|框架定义数据类型|Java|Javascript|Php|Ruby|python|C
|--|--|--|--|--|--|--
|boolean|Boolean|Boolean|Boolean|true、false|Boolean|_Bool
|byte|Byte|String|String|String|String|byte
|int32|Integer|Number|Integer|Fixnum|Integer|long
|int64|Long|Number|Integer|Bignum|Long Integer|long long
|float32|Float|Number|Float|Float|Float|double
|float64|Double|Number|Float|Float|Float|long double
|string|String|String|String|String|String|char array
|void|null|null|NULL|nil|Null|null
|object|Object|Object|Object|Object|Obejct|struct
|list|List|Array|Array|Array|List|array
|hash|Map|Object|Array|Hash|Dictionary|array
|exception|Exception|exception||StandardError
