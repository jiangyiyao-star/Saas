-- ==========================================================
-- SaaS平台数据库初始化脚本 (重构版 - SPU/SKU架构)
-- ==========================================================

-- 创建库
create database if not exists saas;

-- 使用库
use saas;

-- 1. 设备类型表 (device_type)
create table `device_type` (
                               `id` bigint(20) not null auto_increment comment '主键ID',
                               `type_name` varchar(64) not null comment '机型名称（如：双头智能售酒机）',
                               `config_info` varchar(255) default null comment '设备标准配置说明',
                               `key_parameters` json default null comment '关键参数模板(JSON格式)',
                               `category` varchar(64) not null comment '设备分类(如：啤酒机、咖啡机)',
                               `device_code` varchar(64) not null comment '设备编码(用于唯一标识该具体型号)',
                               `is_deleted` tinyint(4) not null default '0' comment '软删除标记(0-正常，1-已删除)',
                               `created_at` datetime not null default current_timestamp comment '创建时间',
                               `updated_at` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
                               primary key (`id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='设备类型基础表';


-- 2. 设备基础信息表 (device_info)
create table `device_info` (
                               `id` bigint(20) not null auto_increment comment '主键ID',
                               `device_sn` varchar(64) not null comment '设备唯一序列号/出厂ID',
                               `device_name` varchar(64) not null comment '设备自定义名称',
                               `type_id` bigint(20) not null comment '关联的设备类型ID (device_type.id)',
                               `owner_id` bigint(20) default null comment '归属店主/商户ID',
                               `status` tinyint(4) not null default '0' comment '设备状态(0-禁用, 1-在线, 2-离线, 3-故障)',
                               `longitude` decimal(10,6) default null comment '经度 (地图分布展示用)',
                               `latitude` decimal(10,6) default null comment '纬度 (地图分布展示用)',
                               `address` varchar(255) default null comment '详细部署物理地址(可由经纬度或IP解析获取)',
                               `ip_address` varchar(64) default null comment 'IP地址 (通过心跳上报获取)',
                               `mac_address` varchar(32) default null comment 'MAC地址 (设备物理网卡地址)',
                               `custom_parameters` json default null comment '设备参数(JSON)',
                               `last_heartbeat` datetime default null comment '最后一次心跳时间',
                               `is_deleted` tinyint(4) not null default '0' comment '软删除标记(0-正常，1-已删除)',
                               `created_at` datetime not null default current_timestamp comment '录入时间',
                               `updated_at` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
                               primary key (`id`),
                               unique key `uk_device_sn` (`device_sn`),
                               key `idx_type_id` (`type_id`),
                               key `idx_owner_id` (`owner_id`),
                               key `idx_status` (`status`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='设备基础信息档案表';


-- 3. 全局商品主表 / SPU表 (product_info)
create table `product_info` (
                                `id` bigint(20) not null auto_increment comment '主键ID',
                                `product_code` varchar(64) not null comment '全局商品编码(系统内部唯一识别)',
                                `product_name` varchar(128) not null comment '商品名称(如：青岛经典生啤)',
                                `brand_id` bigint(20) default null comment '品牌ID (这里简写，实际可关联brand表)',
                                `primary_category` varchar(64) default null comment '一级分类(如：酒水饮料、生鲜)',
                                `sub_category` varchar(64) default null comment '二级分类(如：黄啤、黑啤)',
                                `supplier_id` bigint(20) default null comment '供应商ID',
                                `origin_place` varchar(128) default null comment '产地(如：山东青岛)',
                                `shelf_life_days` int(11) default null comment '保质期(天)',
                                `global_status` tinyint(4) not null default '1' comment '全局上下架状态(1-上架，0-下架)',
                                `image_url` varchar(255) default null comment '商品SPU主图URL',
                                `description` varchar(500) default null comment '商品描述/卖点介绍',
                                `is_deleted` tinyint(4) not null default '0' comment '软删除标记(0-正常，1-已删除)',
                                `created_at` datetime not null default current_timestamp comment '创建时间',
                                `updated_at` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
                                primary key (`id`),
                                unique key `uk_product_code` (`product_code`),
                                key `idx_category` (`primary_category`, `sub_category`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='全局商品主表(SPU)';


-- 4. 商品规格表 / SKU表 (product_sku)
create table `product_sku` (
                               `id` bigint(20) not null auto_increment comment 'SKU主键ID',
                               `product_id` bigint(20) not null comment '关联的商品主表SPU ID',
                               `sku_name` varchar(128) not null comment '规格名称(如：500ml 10°P 瓶装)',
                               `sku_code` varchar(64) default null comment 'SKU编码(具体条形码或内部码)',
                               `alcohol_abv` varchar(20) default null comment '酒精度/麦汁浓度(如：10°P / 4.0%vol)',
                               `capacity` int(11) not null comment '容量/规格(单位：ml)',
                               `base_price` decimal(10,2) not null comment '平台统一指导价',
                               `sku_image_url` varchar(255) default null comment '具体规格图片(可选)',
                               `is_deleted` tinyint(4) not null default '0' comment '软删除标记(0-正常，1-已删除)',
                               `created_at` datetime not null default current_timestamp comment '创建时间',
                               `updated_at` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
                               primary key (`id`),
                               key `idx_product_id` (`product_id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='商品规格表(SKU)';


-- 5. 设备商品配置表 (device_product_config)
create table `device_product_config` (
                                         `id` bigint(20) not null auto_increment comment '主键ID',
                                         `device_sn` varchar(64) not null comment '关联设备序列号',
                                         `channel_no` tinyint(4) not null comment '物理通道号/酒头号(如：1, 2)',
                                         `sku_id` bigint(20) not null comment '关联商品SKU表 ID',
                                         `device_price` decimal(10,2) not null comment '该设备当前的最终售卖价格',
                                         `current_stock` decimal(8,2) not null default '0.00' comment '剩余容量/库存(ml，关联配方联动扣减)',
                                         `created_at` datetime not null default current_timestamp comment '配置创建时间',
                                         `updated_at` datetime not null default current_timestamp on update current_timestamp comment '配置更新时间',
                                         primary key (`id`),
                                         unique key `uk_device_channel` (`device_sn`,`channel_no`),
                                         key `idx_sku_id` (`sku_id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='设备通道与商品SKU挂载配置表';


-- 6. 原料基础信息表 (raw_material)
create table `raw_material` (
                                `id` bigint(20) not null auto_increment comment '主键ID',
                                `material_code` varchar(64) not null comment '原料唯一编码',
                                `material_name` varchar(128) not null comment '原料名称(如：青岛原浆生啤液)',
                                `category` varchar(64) default null comment '原料分类(如：酒水液体、辅助气体)',
                                `customer_id` bigint(20) default '0' comment '归属客户ID (0代表全局; 有值为专供)',
                                `status` tinyint(4) not null default '1' comment '状态(1-正常可用，0-停用下架)',
                                `base_unit` varchar(20) not null comment '基础计量单位(如：L, ML, KG)',
                                `description` varchar(255) default null comment '原料描述/备注',
                                `is_deleted` tinyint(4) not null default '0' comment '软删除标记(0-正常，1-已删除)',
                                `created_at` datetime not null default current_timestamp comment '创建时间',
                                `updated_at` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
                                primary key (`id`),
                                unique key `uk_material_code` (`material_code`),
                                key `idx_customer_id` (`customer_id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='原料基础信息表';


-- 7. 商品配方明细表 (product_formula)
create table `product_formula` (
                                   `id` bigint(20) not null auto_increment comment '主键ID',
                                   `sku_id` bigint(20) not null comment '售卖商品SKU ID',
                                   `raw_material_id` bigint(20) not null comment '消耗原料 ID',
                                   `consume_qty` decimal(10,2) not null comment '消耗数量/比例(如一杯500ml值为500)',
                                   `consume_unit` varchar(20) not null comment '消耗单位(建议与基础计量单位一致)',
                                   `is_core` tinyint(4) not null default '1' comment '是否为核心原料(1-是，0-否)',
                                   `is_deleted` tinyint(4) not null default '0' comment '软删除标记(0-正常，1-已删除)',
                                   `created_at` datetime not null default current_timestamp comment '配置创建时间',
                                   `updated_at` datetime not null default current_timestamp on update current_timestamp comment '配置更新时间',
                                   primary key (`id`),
                                   key `idx_sku_id` (`sku_id`),
                                   key `idx_raw_material_id` (`raw_material_id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='商品配方明细表';


-- 8. 商户商品定价表 (owner_product_price)
create table `owner_product_price` (
                                       `id` bigint(20) not null auto_increment comment '主键ID',
                                       `owner_id` bigint(20) not null comment '厂商/商户ID',
                                       `sku_id` bigint(20) not null comment '关联商品(SKU) ID',
                                       `retail_price` decimal(10,2) not null comment '实际售卖单价',
                                       `owner_status` tinyint(4) not null default '1' comment '独立上下架状态(1-上架，0-下架)',
                                       `created_at` datetime not null default current_timestamp comment '定价创建时间',
                                       `updated_at` datetime not null default current_timestamp on update current_timestamp comment '价格更新时间',
                                       primary key (`id`),
                                       unique key `uk_owner_sku` (`owner_id`,`sku_id`),
                                       key `idx_sku_id` (`sku_id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='商户商品定价表';


-- 9. 设备心跳流水表 (device_heartbeat_log)
create table `device_heartbeat_log` (
                                        `id` bigint(20) not null auto_increment comment '主键',
                                        `device_sn` varchar(64) not null comment '设备序列号',
                                        `report_time` datetime not null comment '上报时间',
                                        `signal_strength` tinyint(4) default null comment '网络信号强度',
                                        `temperature` decimal(5,2) default null comment '设备核心温度',
                                        `ip_address` varchar(64) default null comment '上报时的IP地址',
                                        `created_at` datetime not null default current_timestamp comment '记录生成时间',
                                        primary key (`id`),
                                        key `idx_device_sn` (`device_sn`),
                                        key `idx_report_time` (`report_time`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='设备心跳表';


-- 10. 设备故障与告警表 (device_fault_log)
create table `device_fault_log` (
                                    `id` bigint(20) not null auto_increment comment '主键',
                                    `device_sn` varchar(64) not null comment '设备序列号',
                                    `fault_code` varchar(32) not null comment '硬件故障代码',
                                    `fault_desc` varchar(255) default null comment '故障详细描述',
                                    `severity` tinyint(4) not null comment '严重等级(1-提示, 2-一般, 3-严重)',
                                    `status` tinyint(4) not null default '0' comment '处理状态(0-待处理, 1-处理中, 2-已解决)',
                                    `occur_time` datetime not null comment '故障发生时间',
                                    `resolve_time` datetime default null comment '故障解决时间',
                                    `handler_id` bigint(20) default null comment '处理人员ID',
                                    `resolve_remark` varchar(255) default null comment '处理备注',
                                    `created_at` datetime not null default current_timestamp comment '创建时间',
                                    `updated_at` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
                                    primary key (`id`),
                                    key `idx_device_sn` (`device_sn`),
                                    key `idx_status` (`status`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='设备故障表';


-- ==========================================================
-- 测试数据初始化 (INSERT 语句)
-- ==========================================================

INSERT INTO `device_type` (`type_name`, `config_info`, `key_parameters`, `category`, `device_code`) VALUES
                                                                                                        ('单头台式智能机', '适合小型餐饮桌面摆放，1个制冷酒头', '{"power":"300W"}', '啤酒机', 'BEER-M-V1'),
                                                                                                        ('双头落地智能机', '适合大中型餐厅，2个独立制冷酒头', '{"power":"500W"}', '啤酒机', 'BEER-L-V2'),
                                                                                                        ('三头商用旗舰机', '适合酒吧夜店，3个独立制冷酒头', '{"power":"800W"}', '啤酒机', 'BEER-P-V3'),
                                                                                                        ('四头精酿专属机', '适合精酿酒馆', '{"power":"1000W"}', '精酿机', 'CRAFT-4-V1'),
                                                                                                        ('自助扫码单头机', '纯自助无人工干预，带大屏交互', '{"screen_size":"15.6寸"}', '自助终端', 'SELF-1-V1');


-- SPU (去除了容量和价格，专注通用属性)
INSERT INTO `product_info` (`id`, `product_code`, `product_name`, `brand_id`, `primary_category`, `sub_category`, `origin_place`, `shelf_life_days`) VALUES
                                                                                                                                                         (1, 'SPU-QD-001', '青岛经典生啤', 10, '酒水饮料', '黄啤', '山东青岛', 180),
                                                                                                                                                         (2, 'SPU-XH-001', '雪花纯生啤酒', 11, '酒水饮料', '黄啤', '辽宁沈阳', 180),
                                                                                                                                                         (3, 'SPU-BW-001', '百威金尊', 12, '酒水饮料', '黄啤', '湖北武汉', 365),
                                                                                                                                                         (4, 'SPU-FJ-001', '福佳白啤酒', 13, '酒水饮料', '白啤', '比利时', 365),
                                                                                                                                                         (5, 'SPU-TS-001', '泰山原浆7天', 14, '酒水饮料', '原浆生啤', '山东泰安', 7);

-- SKU (继承原测试数据ID以维持关系，加入了具体容量、酒精度、价格)
INSERT INTO `product_sku` (`id`, `product_id`, `sku_name`, `sku_code`, `alcohol_abv`, `capacity`, `base_price`) VALUES
                                                                                                                    (1, 1, '青岛经典生啤 500ml', 'SKU-QD-500', '4.0%vol', 500, 18.00),
                                                                                                                    (2, 2, '雪花纯生啤酒 500ml', 'SKU-XH-500', '3.6%vol', 500, 15.00),
                                                                                                                    (3, 3, '百威金尊 500ml',     'SKU-BW-500', '4.5%vol', 500, 22.00),
                                                                                                                    (4, 4, '福佳白啤酒 500ml',   'SKU-FJ-500', '4.9%vol', 500, 28.00),
                                                                                                                    (5, 5, '泰山原浆7天 500ml',  'SKU-TS-500', '5.0%vol', 500, 25.00);


INSERT INTO `device_info` (`device_sn`, `device_name`, `type_id`, `owner_id`, `status`, `longitude`, `latitude`, `address`, `ip_address`, `mac_address`) VALUES
                                                                                                                                                             ('SN-A001', '杭州大悦城单头机', 1, 101, 1, 120.123456, 30.123456, '浙江省杭州市拱墅区大悦城B1层', '192.168.1.101', 'AA:BB:CC:DD:EE:01'),
                                                                                                                                                             ('SN-A002', '杭州滨江宝龙双头机', 2, 101, 1, 120.234567, 30.234567, '浙江省杭州市滨江区宝龙城1楼', '192.168.1.102', 'AA:BB:CC:DD:EE:02'),
                                                                                                                                                             ('SN-B001', '上海静安寺台式机', 1, 102, 1, 121.445566, 31.223344, '上海市静安区愚园路1号', '10.0.0.51', '11:22:33:44:55:66'),
                                                                                                                                                             ('SN-B002', '上海新天地双头机', 2, 102, 3, 121.556677, 31.334455, '上海市黄浦区新天地(故障)', '10.0.0.52', '11:22:33:44:55:67');


-- 注意这里 sku_id 直接对应刚刚插入的 product_sku 的 id
INSERT INTO `device_product_config` (`device_sn`, `channel_no`, `sku_id`, `device_price`, `current_stock`) VALUES
                                                                                                               ('SN-A001', 1, 1, 20.00, 15000.00),  -- 通道1卖 青岛500ml
                                                                                                               ('SN-A002', 1, 1, 20.00, 8000.00),
                                                                                                               ('SN-A002', 2, 3, 25.00, 12000.00),  -- 通道2卖 百威500ml
                                                                                                               ('SN-B001', 1, 2, 16.00, 20000.00);  -- 通道1卖 雪花500ml


INSERT INTO `raw_material` (`material_code`, `material_name`, `category`, `customer_id`, `base_unit`) VALUES
                                                                                                          ('RM-QD-001', '青岛经典原浆酒液', '酒水液体', 0, 'ML'),
                                                                                                          ('RM-XH-001', '雪花纯生酒液',   '酒水液体', 0, 'ML'),
                                                                                                          ('RM-GAS-CO2', '食品级二氧化碳气体', '辅助气体', 0, 'KG'),
                                                                                                          ('RM-CUP-500', '定制印花一次性塑料杯', '包装耗材', 0, '个');


-- 这里 sku_id 指向 product_sku 的 ID
INSERT INTO `product_formula` (`sku_id`, `raw_material_id`, `consume_qty`, `consume_unit`, `is_core`) VALUES
                                                                                                          (1, 1, 500.00, 'ML', 1),    -- 卖出1份青岛SKU = 消耗 500ml 青岛原浆
                                                                                                          (1, 3, 0.05,   'KG', 0),    -- 附加消耗 0.05kg CO2
                                                                                                          (1, 4, 1.00,   '个', 0),    -- 附加消耗 1个 杯子
                                                                                                          (2, 2, 500.00, 'ML', 1);    -- 卖出1份雪花SKU = 消耗 500ml 雪花酒液


INSERT INTO `owner_product_price` (`owner_id`, `sku_id`, `retail_price`, `owner_status`) VALUES
                                                                                             (101, 1, 20.00, 1), -- 商户101 把青岛500ml SKU 定价为 20元
                                                                                             (101, 2, 16.00, 1), -- 商户101 把雪花500ml SKU 定价为 16元
                                                                                             (102, 1, 18.00, 1);


-- 补充设备心跳数据 (每条记录模拟不同时间点的状态上报)
INSERT INTO `device_heartbeat_log` (`device_sn`, `report_time`, `signal_strength`, `temperature`, `ip_address`) VALUES
-- 正常设备 SN-A001 的连续心跳 (温度稳定在 3.5度 左右，信号良好)
('SN-A001', DATE_SUB(NOW(), INTERVAL 60 MINUTE), 95, 3.5, '192.168.1.101'),
('SN-A001', DATE_SUB(NOW(), INTERVAL 45 MINUTE), 96, 3.6, '192.168.1.101'),
('SN-A001', DATE_SUB(NOW(), INTERVAL 30 MINUTE), 95, 3.4, '192.168.1.101'),
('SN-A001', DATE_SUB(NOW(), INTERVAL 15 MINUTE), 94, 3.5, '192.168.1.101'),
('SN-A001', NOW(), 95, 3.5, '192.168.1.101'),

-- 正常设备 SN-B001 的连续心跳 (温度稳定，但信号稍弱)
('SN-B001', DATE_SUB(NOW(), INTERVAL 60 MINUTE), 75, 4.2, '10.0.0.51'),
('SN-B001', DATE_SUB(NOW(), INTERVAL 30 MINUTE), 73, 4.3, '10.0.0.51'),
('SN-B001', NOW(), 76, 4.2, '10.0.0.51'),

-- 故障设备 SN-B002 的连续心跳 (模拟制冷故障，温度从 5.5度 飙升到 12.5度)
('SN-B002', DATE_SUB(NOW(), INTERVAL 120 MINUTE), 88, 5.5, '10.0.0.52'),
('SN-B002', DATE_SUB(NOW(), INTERVAL 90 MINUTE), 89, 8.2, '10.0.0.52'),
('SN-B002', DATE_SUB(NOW(), INTERVAL 60 MINUTE), 87, 10.5, '10.0.0.52'),
('SN-B002', DATE_SUB(NOW(), INTERVAL 30 MINUTE), 85, 11.8, '10.0.0.52'),
('SN-B002', NOW(), 86, 12.5, '10.0.0.52');-- 商户102 把青岛500ml SKU 定价为 18元



-- 补充设备故障告警数据
INSERT INTO `device_fault_log` (`device_sn`, `fault_code`, `fault_desc`, `severity`, `status`, `occur_time`, `resolve_time`, `handler_id`, `resolve_remark`) VALUES
-- 严重故障，且处于“待处理(0)”状态
('SN-B002', 'ERR_TEMP_HIGH', '制冷系统异常，核心温度持续偏高超10度', 3, 0, DATE_SUB(NOW(), INTERVAL 2 HOUR), NULL, NULL, NULL),
('SN-B002', 'ERR_CO2_LOW', '辅助气体二氧化碳压力过低，无法出酒', 3, 0, DATE_SUB(NOW(), INTERVAL 3 HOUR), NULL, NULL, NULL),

-- 一般预警，处于“待处理(0)”状态
('SN-A001', 'WARN_STOCK_LOW', '1号酒头(青岛生啤)库存低于10%', 2, 0, DATE_SUB(NOW(), INTERVAL 5 HOUR), NULL, NULL, NULL),

-- 严重故障，处于“处理中(1)”状态 (已有处理人接单，但还未解决完毕)
('SN-A002', 'ERR_COMPRESSOR', '压缩机启动失败，电流过载', 3, 1, DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, 101, '已现场排查，正在等待新压缩机配件调拨更换'),

-- 一般预警，处于“已解决(2)”状态 (已记录解决时间和处理备注)
('SN-B001', 'WARN_NET_WEAK', '网络信号持续弱于80', 1, 2, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 102, '店员已将设备移出死角，信号恢复正常'),
('SN-A001', 'WARN_DOOR_OPEN', '设备下半部储藏柜门未关严', 1, 2, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 101, '联系店员，店员已重新锁紧柜门');