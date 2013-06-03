package org.onetwo.plugins.jdoc.test;

import java.util.HashMap;

import net.yooyo.mall.route.model.BaseEntity;
import net.yooyo.mall.route.model.entity.RouteTeamEntity;
import net.yooyo.mall.route.model.service.impl.RouteTeamServiceImpl;
import net.yooyo.mall.route.model.vo.RouteTeamDeleteParams;
import net.yooyo.mall.route.model.vo.RouteTeamParams;
import net.yooyo.mall.route.model.vo.RouteTeamQueryParams;
import net.yooyo.mall.route.model.vo.RouteTeamResult;
import net.yooyo.mall.route.utils.FastUtils;
import net.yooyo.mall.route.utils.WebConstant.CommonErrorCode;
import net.yooyo.mall.route.utils.WebConstant.TeamErrorCode;

import org.onetwo.common.spring.web.BaseController;
import org.onetwo.plugins.rest.RestResult;
import org.onetwo.plugins.rest.exception.JFishBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/inner")
@Controller
public class TeamController extends BaseController<RouteTeamEntity> {
	@Autowired
	RouteTeamServiceImpl routeTeamServiceImpl;
	
	/**
	 * 具体修改有一个团期的信息。<br/>
	 * 如果id = null，新增团期，根据team_type_id、start_date、state=50查询是否已经存在团期。如果存在返回错误，如果不存在新插入。<br/>
	 * 如果id不为空，根据id查出该团期，更新团期内容。<br/>
	 * generate_method生成方式(1根据团期类型生成，2自己添加)<br/>
	 * @param team
	 * @return
	 * @throws JFishBusinessException
	 */
	@RequestMapping(value = "/route_team_save", method = RequestMethod.POST)
	public RestResult<HashMap<String, Long>> save(RouteTeamParams team) throws JFishBusinessException {
		this.validateAndThrow(team);
		RestResult<HashMap<String, Long>> rs = new RestResult<HashMap<String, Long>>();
		Long teamId = routeTeamServiceImpl.saveOrModify(team);
		HashMap<String, Long> data = new HashMap<String, Long>();
		data.put("id", teamId);
		rs.setData(data);
		rs.markSucceed();
		return rs;
	}
	
	/**
	 * 传入id获取团期信息。或者传入团期类型id，出发日期，获取团期信息。
	 * @param params - true - id : 团期id | team_type_id : 团期类型id | start_date : 出发日期"yyyy-MM-dd"
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/route_team_get", method=RequestMethod.GET)
	public RestResult<RouteTeamResult> teamFetch(RouteTeamQueryParams params){
		boolean validated = validateTeamQueryParams(params);
		RestResult<RouteTeamResult> rs = new RestResult<RouteTeamResult>();
		if (!validated) {
			rs.setError_code(CommonErrorCode.BIZ_ER_ERROR, "id或者团期类型id和出发日期");
			return rs;
		}
		BaseEntity<Long> entity = null;
		if(null == params.getId()){
			entity = routeTeamServiceImpl.findByParams(params);
		} else {
			entity = routeTeamServiceImpl.findById(params.getId());
		}
		if(null == entity){
			rs.setError_code(TeamErrorCode.BIZ_ER_TEAM_NOT_EXIST);
			return rs;
		}
		rs.setData(MappingForResult(entity));
		rs.markSucceed();
		return rs;
	}

	/**
	 * 根据传入的id，设置TB_ROUTE_TEAM线路团期state=-50
	 * @param params - true - id : 团期id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/route_team_delete", method=RequestMethod.GET)
	public RestResult<HashMap<String, Long>> teamDelete(RouteTeamDeleteParams params){
		this.validateAndThrow(params);
		RestResult<HashMap<String, Long>> rs = new RestResult<HashMap<String, Long>>();
		if(null == params.getId() || params.getId() <= 0){
			rs.setError_code(CommonErrorCode.BIZ_ER_ERROR, "团期id");
			return rs;
		}
		BaseEntity<Long> entity = routeTeamServiceImpl.findById(params.getId());
		if(null == entity){
			rs.setError_code(TeamErrorCode.BIZ_ER_TEAM_NOT_EXIST);
			return rs;
		}
		routeTeamServiceImpl.deleteByParams(params);
		HashMap<String, Long> data = new HashMap<String, Long>();
		data.put("id", params.getId());
		rs.setData(data);
		rs.markSucceed();
		return rs;
	}
	
	private RouteTeamResult MappingForResult(BaseEntity<Long> entity){
		return FastUtils.beanDuplicator().map(entity, RouteTeamResult.class);
	}
	
	/**
	 * 传入id获取团期信息。或者传入团期类型id，出发日期，获取团期信息。
	 * @author Matt
	 */
	private boolean validateTeamQueryParams(RouteTeamQueryParams params) {
		if (params.getId() != null || ( params.getStart_date() != null && params.getTeam_type_id() != null )) {
			return true;
		}
		return false;
	}
}
