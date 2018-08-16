update weedcontroller.Rule set expression_=replace(expression_, "r.gavAS(", "r.gavAS('Wewnątrz',") where expression_ like '%r.gavAS(%';
update weedcontroller.Rule set expression_=replace(expression_, "r.gav(", "r.gav('Wewnątrz',") where expression_ like '%r.gav(%';

update weedcontroller.Rule set expression_=replace(expression_, '"HUMI"', '"HUMIDITY"') where expression_ like '%"HUMI"%';
update weedcontroller.Rule set expression_=replace(expression_, '"TEMP"', '"TEMPERATURE"') where expression_ like '%"TEMP"%';

update weedcontroller.Rule set condition_=replace(condition_, "r.gav(", "r.gav('Wewnątrz',") where condition_ like '%r.gav%';
update weedcontroller.Rule set condition_=replace(expression_, '"HUMI"', '"HUMIDITY"') where condition_ like '%"HUMI"%';
