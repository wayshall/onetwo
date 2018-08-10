package org.onetwo.boot.plugins.swagger.mapper;

import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collector;

import org.onetwo.boot.plugins.swagger.entity.SwaggerEntity;
import org.onetwo.boot.plugins.swagger.entity.SwaggerModelEntity;
import org.onetwo.boot.plugins.swagger.entity.SwaggerOperationEntity;
import org.onetwo.boot.plugins.swagger.entity.SwaggerParameterEntity;
import org.onetwo.boot.plugins.swagger.entity.SwaggerResponseEntity;
import org.onetwo.boot.plugins.swagger.model.OperationListModel;
import org.onetwo.boot.plugins.swagger.model.OperationListModel.OperationDetailModel;
import org.onetwo.boot.plugins.swagger.util.SwaggerUtils;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.copier.CopyUtils;

/**
 * @author wayshall
 * <br/>
 */
public class SwaggerModelMapper {
	
	public Swagger map2Swagger(SwaggerEntity swgEntity){
		Swagger swagger = new Swagger();
		CopyUtils.copy(swagger, swgEntity);
		return swagger;
	}
	public OperationListModel map2OperationList(SwaggerOperationEntity op){
		OperationListModel swgOp = new OperationListModel();
		CopyUtils.copy(swgOp, op);
		return swgOp;
	}
	
	public OperationDetailModel map2OperationDetail(SwaggerOperationEntity op){
		OperationDetailModel swgOp = new OperationDetailModel();
		CopyUtils.copy(swgOp, op);
		return swgOp;
	}
	
	public Operation map2Operation(SwaggerOperationEntity op, BiConsumer<SwaggerOperationEntity, Operation> onMapOperation){
		Operation swgOp = new Operation();
		CopyUtils.copy(swgOp, op);
		onMapOperation.accept(op, swgOp);
		return swgOp;
	}
	
	public Map<String, Path> map2Paths(List<SwaggerOperationEntity> ops, BiConsumer<SwaggerOperationEntity, Operation> onMapOperation){
		Map<String, Path> paths = ops.stream().collect(Collector.of(HashMap::new, (acc, op)->{
			Operation opration = map2Operation(op, onMapOperation);
			Path path;
			if(acc.containsKey(op.getPath())){
				path = acc.get(op.getPath());
			}else{
				path = new Path();
				acc.put(op.getPath(), path);
			}
			path.set(op.getRequestMethod().toLowerCase(), opration);
		}, (a,b)->{
			a.putAll(b); 
			return a;
		}));
		return paths;
	}
	
	public Parameter map2Parameter(SwaggerParameterEntity param){
		Class<?> targetType = ReflectUtils.loadClass(param.getJsonType());
		Parameter swgParameter = SwaggerUtils.getJsonMapper().fromJson(param.getJsonData(), targetType);
		return swgParameter;
	}
	
	public Response map2Response(SwaggerResponseEntity response){
		Class<?> targetType = ReflectUtils.loadClass(response.getJsonType());
		Response swgResponse = SwaggerUtils.getJsonMapper().fromJson(response.getJsonData(), targetType);
		return swgResponse;
	}
	
	public Model map2Model(SwaggerModelEntity model){
		Class<?> targetType = ReflectUtils.loadClass(model.getJsonType());
		Model swgModel = SwaggerUtils.getJsonMapper().fromJson(model.getJsonData(), targetType);
		return swgModel;
	}

}
