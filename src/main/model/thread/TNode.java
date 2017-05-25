package main.model.thread;


public class TNode {
        private UserLevelThread running;
        private UserLevelThread blocked;

        public TNode(UserLevelThread running, UserLevelThread blocked){
            this.running = running;
            this.blocked = blocked;
        }

        public UserLevelThread getRunning(){
            return this.running;
        }

        public UserLevelThread getBlocked(){
            return this.blocked;
        }

}
