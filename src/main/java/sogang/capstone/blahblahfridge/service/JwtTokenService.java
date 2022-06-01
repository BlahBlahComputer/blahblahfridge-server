package sogang.capstone.blahblahfridge.service;

import sogang.capstone.blahblahfridge.dto.TokenDTO;

public interface JwtTokenService {

    public String encodeJwtToken(TokenDTO tokenDTO);

}
