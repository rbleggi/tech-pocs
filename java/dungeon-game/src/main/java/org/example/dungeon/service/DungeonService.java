package org.example.dungeon.service;

import org.example.dungeon.dao.DungeonSolutionDao;
import org.example.dungeon.dao.ExposureDao;
import org.example.dungeon.dto.SolveRequest;
import org.example.dungeon.dto.SolveResponse;
import org.example.dungeon.experiment.ExperimentAssigner;
import org.example.dungeon.model.DungeonSolutionEntity;
import org.example.dungeon.model.ExposureEntity;
import org.springframework.stereotype.Service;

@Service
public class DungeonService {

    private final ExperimentAssigner experimentAssigner;
    private final DungeonSolver solver;
    private final DungeonSolutionDao solutionDao;
    private final ExposureDao exposureDao;

    public DungeonService(ExperimentAssigner experimentAssigner,
                          DungeonSolver solver,
                          DungeonSolutionDao solutionDao,
                          ExposureDao exposureDao) {
        this.experimentAssigner = experimentAssigner;
        this.solver = solver;
        this.solutionDao = solutionDao;
        this.exposureDao = exposureDao;
    }

    public SolveResponse solve(SolveRequest request) {
        String variant = experimentAssigner.choose("dungeon_game", request.playerId());
        int minHealth = solver.solve(request.dungeon(), variant);
        exposureDao.save(new ExposureEntity(request.playerId(), "dungeon_game", variant));
        solutionDao.save(new DungeonSolutionEntity(request.playerId(), minHealth, variant));
        return new SolveResponse(request.playerId(), minHealth, variant);
    }
}
