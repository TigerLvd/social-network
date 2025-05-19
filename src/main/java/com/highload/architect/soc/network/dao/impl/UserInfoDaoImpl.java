package com.highload.architect.soc.network.dao.impl;

import com.highload.architect.soc.network.dao.UserInfoDao;
import com.highload.architect.soc.network.model.UserInfo;
import jakarta.persistence.criteria.Root;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoDaoImpl extends AbstractDaoImpl<UserInfo> implements UserInfoDao {
    @Override
    protected Class<UserInfo> getEntityClass() {
        return UserInfo.class;
    }

    @Override
    public List<UserInfo> findByFirstNameAndSecondName(String firstName, String secondName) {
        HibernateCriteriaBuilder builder = getCriteriaBuilder();
        JpaCriteriaQuery<UserInfo> query = builder.createQuery(getEntityClass());
        Root<UserInfo> root = query.from(getEntityClass());

        query.where(
                        builder.and(
                                builder.like(root.get("firstName"), firstName + "%"),
                                builder.like(root.get("secondName"), secondName + "%")
                        )
                )
                .orderBy(builder.desc(root.get("id")));

        return getSession().createQuery(query).list();
    }
}
