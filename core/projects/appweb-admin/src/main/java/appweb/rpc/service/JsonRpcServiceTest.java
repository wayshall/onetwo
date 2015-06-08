package appweb.rpc.service;

import java.util.List;

import org.onetwo.common.jsonrpc.annotation.JsonRpcService;
import org.onetwo.common.utils.Page;

import appweb.rpc.vo.UserVo;

@JsonRpcService
public interface JsonRpcServiceTest {
	public String say(String something);
	public Long sum(int num1, int num2);

	public List<UserVo> findUsersByUserNameLike(String userName, int limited);
	public Page<UserVo> findUsersPageByUserNameLike(String userName, int limited);
}