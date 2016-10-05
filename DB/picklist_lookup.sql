insert into picklist_lookup (id, lookup_name, lookup_value, parent_lookup_name, parent_lookup_value) values 
	(20, 'MARKET_SEGMENT', 'Motion', NULL, NULL),
	(21, 'MARKET_SEGMENT', 'Microphone', NULL, NULL),
	(22, 'SUB_SEGMENT', 'Smartphone', 'MARKET_SEGMENT', 'Motion'),
	(23, 'SUB_SEGMENT', 'Tablet', 'MARKET_SEGMENT', 'Motion'),
	(24, 'SUB_SEGMENT', 'Gaming', 'MARKET_SEGMENT', 'Motion'),
	(25, 'SUB_SEGMENT', 'Remote', 'MARKET_SEGMENT', 'Motion'),
	(26, 'SUB_SEGMENT', 'OIS', 'MARKET_SEGMENT', 'Motion'),
	(27, 'SUB_SEGMENT', 'Wearable', 'MARKET_SEGMENT', 'Motion'),
	(28, 'SUB_SEGMENT', 'Automotive', 'MARKET_SEGMENT', 'Motion'),
	(29, 'SUB_SEGMENT', 'Non-Cnsmr/Indstl', 'MARKET_SEGMENT', 'Motion'),
	(30, 'SUB_SEGMENT', 'Hearing Aid', 'MARKET_SEGMENT', 'Microphone'),
	(31, 'SUB_SEGMENT', 'A/V Conferencing', 'MARKET_SEGMENT', 'Microphone'),
	(32, 'SUB_SEGMENT', 'Distributor', 'MARKET_SEGMENT', 'Microphone'),
	(33, 'SUB_SEGMENT', 'Other', 'MARKET_SEGMENT', 'Microphone'),
	(34, 'SUB_SEGMENT', 'Toy', 'MARKET_SEGMENT', 'Motion'),
	(35, 'SUB_SEGMENT', 'Distributor', 'MARKET_SEGMENT', 'Motion'),
	(36, 'SUB_SEGMENT', 'Other', 'MARKET_SEGMENT', 'Motion'),
	(37, 'SUB_SEGMENT', 'Smartphone', 'MARKET_SEGMENT', 'Microphone'),
	(38, 'SUB_SEGMENT', 'Tablet', 'MARKET_SEGMENT', 'Microphone'),
	(39, 'SUB_SEGMENT', 'Gaming', 'MARKET_SEGMENT', 'Microphone'),
	(40, 'SUB_SEGMENT', 'Remote', 'MARKET_SEGMENT', 'Microphone'),
	(41, 'SUB_SEGMENT', 'Camera', 'MARKET_SEGMENT', 'Microphone'),
	(42, 'SUB_SEGMENT', 'Wearable', 'MARKET_SEGMENT', 'Microphone'),
	(43, 'SUB_SEGMENT', 'Automotive', 'MARKET_SEGMENT', 'Microphone'),
	(44, 'SUB_SEGMENT', 'Non-Cnsmr/Indstl', 'MARKET_SEGMENT', 'Microphone'),
	(45, 'BU', 'Wearable', NULL, NULL),
	(46, 'BU', 'Industrial/Automotive', NULL, NULL),
	(47, 'BU', 'OIS', NULL, NULL),
	(48, 'BU', 'Handset/Smartphone', NULL, NULL),
	(49, 'BU', 'Gaming', NULL, NULL),
	(50, 'BU', 'Healthcare', NULL, NULL),
	(51, 'BU', 'Remote', NULL, NULL);

delete from picklist_lookup where lookup_name='MARKET_SEGMENT' OR  lookup_name='SUB_SEGMENT';

insert into picklist_lookup (lookup_name, lookup_value, parent_lookup_name, parent_lookup_value) values 
	('MARKET_SEGMENT', 'Mobile', NULL, NULL),
	('MARKET_SEGMENT', 'Wearable', NULL, NULL),
	('MARKET_SEGMENT', 'Consumer & IoT', NULL, NULL),
	('MARKET_SEGMENT', 'Automotive', NULL, NULL),
	('MARKET_SEGMENT', 'Coursa', NULL, NULL);
	
insert into picklist_lookup (lookup_name, lookup_value, parent_lookup_name, parent_lookup_value) values 
	('SUB_SEGMENT', 'Handset', 'MARKET_SEGMENT', 'Mobile'),
	('SUB_SEGMENT', 'Tablet/Laptop/Notebook/PC/2&1', 'MARKET_SEGMENT', 'Mobile'),
	('SUB_SEGMENT', 'Head', 'MARKET_SEGMENT', 'Wearable'),
	('SUB_SEGMENT', 'HMD/VR', 'MARKET_SEGMENT', 'Wearable'),
	('SUB_SEGMENT', 'Wrist', 'MARKET_SEGMENT', 'Wearable'),
	('SUB_SEGMENT', 'Other Body', 'MARKET_SEGMENT', 'Wearable'),
	('SUB_SEGMENT', 'Toy', 'MARKET_SEGMENT', 'Consumer & IoT'),
	('SUB_SEGMENT', 'Drone', 'MARKET_SEGMENT', 'Consumer & IoT'),
	('SUB_SEGMENT', 'Home', 'MARKET_SEGMENT', 'Consumer & IoT'),
	('SUB_SEGMENT', 'Health/Medical', 'MARKET_SEGMENT', 'Consumer & IoT'),
	('SUB_SEGMENT', 'Industrial', 'MARKET_SEGMENT', 'Consumer & IoT'),
	('SUB_SEGMENT', 'Sport', 'MARKET_SEGMENT', 'Consumer & IoT'),
	('SUB_SEGMENT', 'Gaming Console', 'MARKET_SEGMENT', 'Consumer & IoT'),
	('SUB_SEGMENT', 'Camera', 'MARKET_SEGMENT', 'Consumer & IoT'),
	('SUB_SEGMENT', 'In-Cabin', 'MARKET_SEGMENT', 'Automotive'),
	('SUB_SEGMENT', 'Functional Safety', 'MARKET_SEGMENT', 'Automotive'),
	('SUB_SEGMENT', 'Retail', 'MARKET_SEGMENT', 'Coursa'),
	('SUB_SEGMENT', 'Sport', 'MARKET_SEGMENT', 'Coursa');
	