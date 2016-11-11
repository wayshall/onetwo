package org.onetwo.ext.permission;

import org.onetwo.ext.permission.api.PermissionConfig;
import org.onetwo.ext.permission.entity.DefaultIPermission;

abstract public class PermissionConfigAdapter<P extends DefaultIPermission<P>> implements PermissionConfig<P> {
}
