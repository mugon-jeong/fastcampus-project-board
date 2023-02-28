package com.fastcampus.projectboard.domain;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
// callSuper AuditingFields 포함여부
@ToString(callSuper = true)
@Table(
    indexes = {
        // column에서 unique 사용하는 것과 동일
        @Index(columnList = "hashtagName", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
    }
)
@Entity
public class Hashtag extends AuditingFields{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 중복 방지를 위해 Set 사용
  // 추후 정렬을 위해 순서가 있는 LinkedHashSet 사용
  @ToString.Exclude // 순환 참조 방지
  @ManyToMany(mappedBy = "hashtags")
  private Set<Article> articles = new LinkedHashSet<>();
  @Setter @Column(nullable = false)
  private String hashtagName; // 해시태그 이름

  protected Hashtag() {}

  private Hashtag(String hashtagName) {
    this.hashtagName = hashtagName;
  }

  public static Hashtag of(String hashtagName) {
    return new Hashtag(hashtagName);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Hashtag that)) {
      return false;
    }
    return this.getId() != null && this.getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }
}
