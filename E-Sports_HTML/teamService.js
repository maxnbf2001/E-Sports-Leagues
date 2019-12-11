angular.module('esports').service('teamService', teamService);

function teamService(){
    this.getLeagueNames = function(){
        return ["blue", "gold"]
    };
}