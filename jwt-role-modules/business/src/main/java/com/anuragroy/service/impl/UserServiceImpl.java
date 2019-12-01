package com.anuragroy.service.impl;

import com.anuragroy.dto.UserDto;
import com.anuragroy.model.Role;
import com.anuragroy.model.User;
import com.anuragroy.repository.RoleRepository;
import com.anuragroy.repository.UserRepository;
import com.anuragroy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {
	
	@Autowired
	private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(username);
		if(user == null){
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassWord(), getAuthority(user));
	}

	private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		user.getRoles().forEach(role -> {
			//authorities.add(new SimpleGrantedAuthority(role.getName()));
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
		});
		return authorities;
		//return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	public List<User> findAll() {
		List<User> list = new ArrayList<>();
		userRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public void delete(long id) {
		userRepository.deleteById(id);
	}

	@Override
	public User findOne(String username) {
		return userRepository.findByUserName(username);
	}

	@Override
	public User findById(Long id) {
		return userRepository.findById(id).get();
	}

	@Override
    public User save(UserDto user) {
	    User newUser = new User();
	    newUser.setUserName(user.getUserName());
		newUser.setPassWord(bcryptEncoder.encode(user.getPassWord()));
		newUser.setEmail(user.getEmail());
		newUser.setFullName(user.getFullName());
		newUser.setActive(true);

		Set<Role> roles = new HashSet<>();
		String userRoles = user.getRoles();
		if(userRoles != null) {
			if (!userRoles.isEmpty()) {
				String[] roleList = userRoles.split(",");
				for (String role : roleList) {
					Role theRole = roleRepository.findByName(role);
					roles.add(theRole);
				}
				newUser.setRoles(roles);
			} else {
				Role theRole = roleRepository.findByName("USER");
				newUser.setRoles(new HashSet<Role>(Arrays.asList(theRole)));
			}
		} else {
			Role theRole = roleRepository.findByName("USER");
			newUser.setRoles(new HashSet<Role>(Arrays.asList(theRole)));
		}

        return userRepository.save(newUser);
    }
}
