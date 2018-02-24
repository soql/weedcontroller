update weedcontroller.SensorResultLog set humidity=humidity*100 where sensor_number=3 and humidity<1;

update weedcontroller.SensorResultLog set humidity=((((humidity-100)/100)*(2000-400))-400)*-1 where sensor_number=3 and humidity<100;