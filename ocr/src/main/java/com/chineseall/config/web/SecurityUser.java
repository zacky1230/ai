package com.chineseall.config.web;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 权限用户
 *
 * @author codelife
 */
public class SecurityUser implements Serializable {
	private Long userId;
	private String username;
	private String realName;
	private Set<String> roles = new HashSet<>();
	private Set<String> permissions = new HashSet<>();


	public void addRole(String role) {
		roles.add(role);
	}

	public void addPermission(String permission) {
		roles.add(permission);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}
}
