
package com.skiwi.githubhooksechatservice.github.events;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Frank van Heeswijk
 */
public final class ForkEvent {
	@JsonProperty
	private Repository forkee;
	
	@JsonProperty
	private Repository repository;
	
	@JsonProperty
	private User sender;

	public Repository getForkee() {
		return forkee;
	}

	public Repository getRepository() {
		return repository;
	}

	public User getSender() {
		return sender;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 11 * hash + Objects.hashCode(this.forkee);
		hash = 11 * hash + Objects.hashCode(this.repository);
		hash = 11 * hash + Objects.hashCode(this.sender);
		return hash;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ForkEvent other = (ForkEvent)obj;
		if (!Objects.equals(this.forkee, other.forkee)) {
			return false;
		}
		if (!Objects.equals(this.repository, other.repository)) {
			return false;
		}
		if (!Objects.equals(this.sender, other.sender)) {
			return false;
		}
		return true;
	}
}