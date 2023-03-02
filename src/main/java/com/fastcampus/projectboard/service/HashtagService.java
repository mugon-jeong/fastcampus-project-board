package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Hashtag;
import com.fastcampus.projectboard.repository.HashtagRepository;
import com.fastcampus.projectboard.repository.querydsl.HashtagRepositoryCustom;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class HashtagService {

  private final HashtagRepository hashtagRepository;

  public HashtagService(@Qualifier("hashtagRepository") HashtagRepository hashtagRepository) {
    this.hashtagRepository = hashtagRepository;
  }

  public Set<String> parseHashtagNames(String content) {
    if(content == null){
      return Set.of();
    }

    Pattern pattern = Pattern.compile("#[\\w가-힣]+");
    Matcher matcher = pattern.matcher(content.strip());
    Set<String> result = new HashSet<>();
    while (matcher.find()){
      result.add(matcher.group().replace("#",""));
    }
    return Set.copyOf(result);
  }

  public Set<Hashtag> findHashtagsByNames(Set<String> hashtagNames) {
    return new HashSet<>(hashtagRepository.findByHashtagNameIn(hashtagNames));
  }

  public void deleteHashtagWithoutArticles(Long hashtagId) {
    Hashtag hashtag = hashtagRepository.getReferenceById(hashtagId);
    if(hashtag.getArticles().isEmpty()){
      hashtagRepository.delete(hashtag);
    }
  }
}
