package com.apptive.gameservice;

import com.apptive.Model.Generation;
import org.springframework.stereotype.Service;

/**
 * Created by apptive on 2017. 02. 02..
 */
public interface GameService {

      Generation getNextGeneration(Generation generation);
}
