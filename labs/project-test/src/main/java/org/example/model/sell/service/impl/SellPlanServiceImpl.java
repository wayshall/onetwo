package org.example.model.sell.service.impl;
import org.springframework.stereotype.Service;
import org.onetwo.common.hibernate.HibernateCrudServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import org.example.model.sell.entity.SellPlanEntity;

@Transactional
@Service
public class SellPlanServiceImpl extends HibernateCrudServiceImpl<SellPlanEntity, Long> {

}