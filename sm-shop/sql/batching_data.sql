update salesmanager.merchant_store set zone_id = 1 where merchant_id = 1;


delete from salesmanager.country_description where language_id=3
delete from salesmanager.zone_description where language_id=3
delete from salesmanager.language where language_id=3