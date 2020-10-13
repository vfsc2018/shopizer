update salesmanager.merchant_store set zone_id = 1 where merchant_id = 1;

ALTER TABLE salesmanager.customer DROP COLUMN billing_last_name;

ALTER TABLE salesmanager.customer
    RENAME billing_first_name TO billing_full_name;

ALTER TABLE salesmanager.customer
    ALTER COLUMN billing_full_name TYPE character varying(150) COLLATE pg_catalog."default";
    
    
ALTER TABLE salesmanager.customer DROP COLUMN delivery_last_name;

ALTER TABLE salesmanager.customer
    ALTER COLUMN delivery_first_name TYPE character varying(150) COLLATE pg_catalog."default";