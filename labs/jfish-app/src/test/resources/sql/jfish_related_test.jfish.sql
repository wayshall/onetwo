@jfishRelatedTest.find.orderitems = select * from t_item i join t_order_item oi on (i.id=oi.item_id) where oi.order_id=:order_id order by i.id
@jfishRelatedTest.find.orderitems.mapped.entity = org.example.app.model.member.entity.ItemEntity

@jfishRelatedTest.delete.orderitems = delete from t_order_item

