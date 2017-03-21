# Luna RPC
一款参考自dubbo和motan的微服务框架。

# Features
## 自定义通讯协议luna，可自行拓展
| 8bit | 8bit  | 8bit  | 8bit
|------|------ |------ |-----
| 00~07| 08~15 | 16~23 | 24~31
|magic | <<    | flag  | status
| 32~39| 40~47 | 48~55 | 56~63
|request_id| << | << | <<
| 64~71| 72~79 | 80~87 | 88~95
| << | << | << | <<
|96~103|104~111|112~119|120~127
|body_length| << | << | <<
flag包含：hb（心跳标示）、ow(OneWay)、rp（请求还是响应）

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