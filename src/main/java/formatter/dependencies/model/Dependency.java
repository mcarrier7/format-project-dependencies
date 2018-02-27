/**
 * 
 */
package formatter.dependencies.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

/**
 *
 */
@SuppressWarnings("serial")
public class Dependency implements
		Serializable
{
	private String groupId;
	private String artifactId;
	private String version;
	private String scope;

	private static final String CONSTANT_GROUPID = "groupId";
	private static final String CONSTANT_ARTIFACTID = "artifactId";
	private static final String CONSTANT_VERSION = "version";
	private static final String CONSTANT_SCOPE = "scope";

	public Dependency(
			String groupId,
			String artifactId,
			String version,
			String scope ) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
		this.scope = scope;
	}

	public Dependency(
			JSONObject dependencyJSON ) {
		if (dependencyJSON != null) {
			this.groupId = dependencyJSON.has(CONSTANT_GROUPID) ? dependencyJSON.getString(CONSTANT_GROUPID) : null;
			this.artifactId = dependencyJSON.has(CONSTANT_ARTIFACTID) ? dependencyJSON.getString(CONSTANT_ARTIFACTID)
					: null;
			this.scope = dependencyJSON.has(CONSTANT_SCOPE) ? dependencyJSON.getString(CONSTANT_SCOPE) : null;
			this.version = dependencyJSON.has(CONSTANT_VERSION) ? ("" + dependencyJSON.get(CONSTANT_VERSION)) : null;
		}
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(
			String groupId ) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(
			String artifactId ) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(
			String version ) {
		this.version = version;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(
			String scope ) {
		this.scope = scope;
	}

	@Override
	public String toString() {
		return toJSON().toString();
	}

	public JSONObject toJSON() {
		JSONObject retObj = new JSONObject();
		retObj.put(
				CONSTANT_GROUPID,
				this.groupId);
		retObj.put(
				CONSTANT_ARTIFACTID,
				this.artifactId);
		retObj.put(
				CONSTANT_VERSION,
				this.version);
		retObj.put(
				CONSTANT_SCOPE,
				this.scope);
		return retObj;
	}

	public boolean compareTo(
			Dependency dependency ) {
		boolean match = true;
		if (!this.groupId.equals(dependency.getGroupId())) {
			match = false;
		}
		if (!this.artifactId.equals(dependency.getArtifactId())) {
			match = false;
		}
		if (!this.version.equals(dependency.getVersion())) {
			match = false;
		}
		if (!this.scope.equals(dependency.getScope())) {
			List<String> nonTestList = new ArrayList<String>();
			nonTestList.add("compile");
			nonTestList.add("provided");
			boolean nonTest = nonTestList.contains(this.scope) && nonTestList.contains(dependency.getScope());
			if (!nonTest) {
				match = false;
			}
		}
		return match;
	}
}
